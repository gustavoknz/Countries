package dev.gustavo.countries.data.local.dao;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import dev.gustavo.countries.data.local.database.StringListConverter;
import dev.gustavo.countries.data.local.entity.CountryDetailEntity;
import java.lang.Class;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class CountryDetailDao_Impl implements CountryDetailDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<CountryDetailEntity> __insertAdapterOfCountryDetailEntity;

  private final StringListConverter __stringListConverter = new StringListConverter();

  public CountryDetailDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfCountryDetailEntity = new EntityInsertAdapter<CountryDetailEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `country_details` (`cca3`,`commonName`,`officialName`,`capital`,`flagUrl`,`region`,`subregion`,`languages`,`population`,`borders`,`currencies`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final CountryDetailEntity entity) {
        if (entity.getCca3() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getCca3());
        }
        if (entity.getCommonName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getCommonName());
        }
        if (entity.getOfficialName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getOfficialName());
        }
        if (entity.getCapital() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getCapital());
        }
        if (entity.getFlagUrl() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getFlagUrl());
        }
        if (entity.getRegion() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getRegion());
        }
        if (entity.getSubregion() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getSubregion());
        }
        final String _tmp = __stringListConverter.fromList(entity.getLanguages());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, _tmp);
        }
        statement.bindLong(9, entity.getPopulation());
        final String _tmp_1 = __stringListConverter.fromList(entity.getBorders());
        if (_tmp_1 == null) {
          statement.bindNull(10);
        } else {
          statement.bindText(10, _tmp_1);
        }
        final String _tmp_2 = __stringListConverter.fromList(entity.getCurrencies());
        if (_tmp_2 == null) {
          statement.bindNull(11);
        } else {
          statement.bindText(11, _tmp_2);
        }
      }
    };
  }

  @Override
  public Object insert(final CountryDetailEntity detail,
      final Continuation<? super Unit> $completion) {
    if (detail == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfCountryDetailEntity.insert(_connection, detail);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object getByCode(final String cca3,
      final Continuation<? super CountryDetailEntity> $completion) {
    final String _sql = "SELECT * FROM country_details WHERE cca3 = ?";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (cca3 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, cca3);
        }
        final int _columnIndexOfCca3 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cca3");
        final int _columnIndexOfCommonName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "commonName");
        final int _columnIndexOfOfficialName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "officialName");
        final int _columnIndexOfCapital = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "capital");
        final int _columnIndexOfFlagUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "flagUrl");
        final int _columnIndexOfRegion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "region");
        final int _columnIndexOfSubregion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "subregion");
        final int _columnIndexOfLanguages = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "languages");
        final int _columnIndexOfPopulation = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "population");
        final int _columnIndexOfBorders = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "borders");
        final int _columnIndexOfCurrencies = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "currencies");
        final CountryDetailEntity _result;
        if (_stmt.step()) {
          final String _tmpCca3;
          if (_stmt.isNull(_columnIndexOfCca3)) {
            _tmpCca3 = null;
          } else {
            _tmpCca3 = _stmt.getText(_columnIndexOfCca3);
          }
          final String _tmpCommonName;
          if (_stmt.isNull(_columnIndexOfCommonName)) {
            _tmpCommonName = null;
          } else {
            _tmpCommonName = _stmt.getText(_columnIndexOfCommonName);
          }
          final String _tmpOfficialName;
          if (_stmt.isNull(_columnIndexOfOfficialName)) {
            _tmpOfficialName = null;
          } else {
            _tmpOfficialName = _stmt.getText(_columnIndexOfOfficialName);
          }
          final String _tmpCapital;
          if (_stmt.isNull(_columnIndexOfCapital)) {
            _tmpCapital = null;
          } else {
            _tmpCapital = _stmt.getText(_columnIndexOfCapital);
          }
          final String _tmpFlagUrl;
          if (_stmt.isNull(_columnIndexOfFlagUrl)) {
            _tmpFlagUrl = null;
          } else {
            _tmpFlagUrl = _stmt.getText(_columnIndexOfFlagUrl);
          }
          final String _tmpRegion;
          if (_stmt.isNull(_columnIndexOfRegion)) {
            _tmpRegion = null;
          } else {
            _tmpRegion = _stmt.getText(_columnIndexOfRegion);
          }
          final String _tmpSubregion;
          if (_stmt.isNull(_columnIndexOfSubregion)) {
            _tmpSubregion = null;
          } else {
            _tmpSubregion = _stmt.getText(_columnIndexOfSubregion);
          }
          final List<String> _tmpLanguages;
          final String _tmp;
          if (_stmt.isNull(_columnIndexOfLanguages)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getText(_columnIndexOfLanguages);
          }
          _tmpLanguages = __stringListConverter.toList(_tmp);
          final long _tmpPopulation;
          _tmpPopulation = _stmt.getLong(_columnIndexOfPopulation);
          final List<String> _tmpBorders;
          final String _tmp_1;
          if (_stmt.isNull(_columnIndexOfBorders)) {
            _tmp_1 = null;
          } else {
            _tmp_1 = _stmt.getText(_columnIndexOfBorders);
          }
          _tmpBorders = __stringListConverter.toList(_tmp_1);
          final List<String> _tmpCurrencies;
          final String _tmp_2;
          if (_stmt.isNull(_columnIndexOfCurrencies)) {
            _tmp_2 = null;
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfCurrencies);
          }
          _tmpCurrencies = __stringListConverter.toList(_tmp_2);
          _result = new CountryDetailEntity(_tmpCca3,_tmpCommonName,_tmpOfficialName,_tmpCapital,_tmpFlagUrl,_tmpRegion,_tmpSubregion,_tmpLanguages,_tmpPopulation,_tmpBorders,_tmpCurrencies);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
