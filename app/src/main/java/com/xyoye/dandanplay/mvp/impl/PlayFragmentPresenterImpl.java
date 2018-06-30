package com.xyoye.dandanplay.mvp.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.xyoye.core.base.BaseMvpPresenter;
import com.xyoye.core.db.DataBaseInfo;
import com.xyoye.core.db.DataBaseManager;
import com.xyoye.core.rx.Lifeful;
import com.xyoye.dandanplay.bean.FolderBean;
import com.xyoye.dandanplay.mvp.view.PlayFragmentView;
import com.xyoye.dandanplay.mvp.presenter.PlayFragmentPresenter;
import com.xyoye.dandanplay.utils.Config;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YE on 2018/6/29 0029.
 */


public class PlayFragmentPresenterImpl extends BaseMvpPresenter<PlayFragmentView> implements PlayFragmentPresenter {

    public PlayFragmentPresenterImpl(PlayFragmentView view, Lifeful lifeful) {
        super(view, lifeful);
    }

    @Override
    public void init() {

    }

    @Override
    public void process(Bundle savedInstanceState) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void searchFolder() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/test");
        Observable.just(file)
                .flatMap(new Function<File, Observable<File>>() {
                    @Override
                    public Observable<File> apply(File file) throws Exception {
                        return listFiles(file);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onNext(File file) {
                                    String folderPath = FileUtils.getDirName(file);
                                    String fileName = FileUtils.getFileName(file);
                                    saveData(folderPath,fileName);
                               }

                               @Override
                               public void onError(Throwable e) {
                                    e.printStackTrace();
                               }

                               @Override
                               public void onComplete() {
                                   getView().refreshAdapter(getFolderList());
                               }
                           }
                );
    }

    /**
     * RxJava递归查询内存中的视频文件
     */
    private Observable<File> listFiles(final File f){
        if(f.isDirectory()){
            return Observable.fromArray(f.listFiles()).flatMap(new Function<File, Observable<File>>() {
                @Override
                public Observable<File> apply(File file) {
                    return listFiles(file);
                }
            });
        } else {
            return Observable.just(f).filter(new Predicate<File>() {
                @Override
                public boolean test(File file) throws Exception {
                    return f.exists() && f.canRead() && isVideo(f);
                }
            });
        }
    }

    private static boolean isVideo(File file){
        String path = file.getAbsolutePath();
        String ext = FileUtils.getFileExtension(path).toUpperCase();
        return Config.videoType.contains(ext);
    }

    private void saveData(String folderPath, String fileName){
        ContentValues values=new ContentValues();
        values.put(DataBaseInfo.getFieldNames()[2][1],folderPath);
        values.put(DataBaseInfo.getFieldNames()[2][2],fileName);
        SQLiteDatabase sqLiteDatabase = DataBaseManager.getInstance().getSQLiteDatabase();
        String sql = "SELECT * FROM "+DataBaseInfo.getTableNames()[2]+
                " WHERE "+DataBaseInfo.getFieldNames()[2][1]+ "=? " +
                "AND "+DataBaseInfo.getFieldNames()[2][2]+ "=? ";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{folderPath, fileName});
        if (cursor.moveToNext()){
            cursor.close();
            sqLiteDatabase.update(DataBaseInfo.getTableNames()[2],values,null,null);
        }else {
            sqLiteDatabase.insert(DataBaseInfo.getTableNames()[2],null,values);
        }

    }

    private List<FolderBean> getFolderList(){
        List<FolderBean> folderBeanList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = DataBaseManager.getInstance().getSQLiteDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT folder_path,count(folder_path) AS number FROM file GROUP BY folder_path",new String[]{});
        while (cursor.moveToNext()){
            String folderPath = cursor.getString(0);
            int fileNumber = cursor.getInt(1);
            folderBeanList.add(new FolderBean(folderPath,fileNumber));

            ContentValues values = new ContentValues();
            values.put(DataBaseInfo.getFieldNames()[1][1],folderPath);
            values.put(DataBaseInfo.getFieldNames()[1][2],fileNumber);
            sqLiteDatabase.update(DataBaseInfo.getTableNames()[1], values,null,null);
        }
        cursor.close();
        return folderBeanList;
    }
}
