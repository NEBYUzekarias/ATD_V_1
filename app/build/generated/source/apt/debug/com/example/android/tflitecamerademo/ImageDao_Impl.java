package com.example.android.tflitecamerademo;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings("unchecked")
public final class ImageDao_Impl implements ImageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfImage;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfImage;

  private final SharedSQLiteStatement __preparedStmtOfStage;

  public ImageDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfImage = new EntityInsertionAdapter<Image>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `image_table`(`id`,`stage`,`is_upload`,`path`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Image value) {
        stmt.bindLong(1, value.id);
        stmt.bindLong(2, value.stage);
        final int _tmp;
        _tmp = value.isUpload ? 1 : 0;
        stmt.bindLong(3, _tmp);
        if (value.imagePath == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.imagePath);
        }
      }
    };
    this.__deletionAdapterOfImage = new EntityDeletionOrUpdateAdapter<Image>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `image_table` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Image value) {
        stmt.bindLong(1, value.id);
      }
    };
    this.__preparedStmtOfStage = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE image_table SET stage = ? where id = ?";
        return _query;
      }
    };
  }

  @Override
  public void insertImage(final Image image) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfImage.insert(image);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deletImage(final Image image) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfImage.handle(image);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void stage(final int stage, final int id) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfStage.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, stage);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfStage.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Image>> getAll() {
    final String _sql = "select * from image_table order by is_upload";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"image_table"}, new Callable<List<Image>>() {
      @Override
      public List<Image> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStage = CursorUtil.getColumnIndexOrThrow(_cursor, "stage");
          final int _cursorIndexOfIsUpload = CursorUtil.getColumnIndexOrThrow(_cursor, "is_upload");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "path");
          final List<Image> _result = new ArrayList<Image>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Image _item;
            _item = new Image();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            _item.stage = _cursor.getInt(_cursorIndexOfStage);
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsUpload);
            _item.isUpload = _tmp != 0;
            _item.imagePath = _cursor.getString(_cursorIndexOfImagePath);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }
}
