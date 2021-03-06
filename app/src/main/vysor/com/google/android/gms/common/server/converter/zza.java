// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server.converter;

import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<ConverterWrapper>
{
    static void zza(final ConverterWrapper converterWrapper, final Parcel parcel, final int n) {
        final int zzcr = zzb.zzcr(parcel);
        zzb.zzc(parcel, 1, converterWrapper.getVersionCode());
        zzb.zza(parcel, 2, (Parcelable)converterWrapper.zzavn(), n, false);
        zzb.zzaj(parcel, zzcr);
    }
    
    public ConverterWrapper zzct(final Parcel parcel) {
        final int zzcq = com.google.android.gms.common.internal.safeparcel.zza.zzcq(parcel);
        int zzg = 0;
        StringToIntConverter stringToIntConverter = null;
        while (parcel.dataPosition() < zzcq) {
            final int zzcp = com.google.android.gms.common.internal.safeparcel.zza.zzcp(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzgv(zzcp)) {
                default: {
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzcp);
                    continue;
                }
                case 1: {
                    zzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzcp);
                    continue;
                }
                case 2: {
                    stringToIntConverter = com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzcp, (android.os.Parcelable$Creator<StringToIntConverter>)StringToIntConverter.CREATOR);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzcq) {
            throw new com.google.android.gms.common.internal.safeparcel.zza.zza(new StringBuilder(37).append("Overread allowed size end=").append(zzcq).toString(), parcel);
        }
        return new ConverterWrapper(zzg, stringToIntConverter);
    }
    
    public ConverterWrapper[] zzgy(final int n) {
        return new ConverterWrapper[n];
    }
}
