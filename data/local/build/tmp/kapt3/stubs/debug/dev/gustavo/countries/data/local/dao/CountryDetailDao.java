package dev.gustavo.countries.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\n\u00a8\u0006\u000b"}, d2 = {"Ldev/gustavo/countries/data/local/dao/CountryDetailDao;", "", "getByCode", "Ldev/gustavo/countries/data/local/entity/CountryDetailEntity;", "cca3", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "", "detail", "(Ldev/gustavo/countries/data/local/entity/CountryDetailEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "local_debug"})
@androidx.room.Dao()
public abstract interface CountryDetailDao {
    
    @androidx.room.Query(value = "SELECT * FROM country_details WHERE cca3 = :cca3")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByCode(@org.jetbrains.annotations.NotNull()
    java.lang.String cca3, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super dev.gustavo.countries.data.local.entity.CountryDetailEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.data.local.entity.CountryDetailEntity detail, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}