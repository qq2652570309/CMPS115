package com.example.foodiemore.foodiemore.Merchant_Info_Data;

import android.provider.BaseColumns;

/**
 * Created by harlan on 2017/5/24.
 */

public final class MerchantInfoContract {
    // Constructor
    private MerchantInfoContract(){}

    public static class MerchantInfoEntry implements BaseColumns {
        // 数据表格title为MerchantInfo
        public static final String TABLE_NAME = "MerchantInfo";

        public static final String COLUMN_MERCHANT_UID = "uid";

        public static final String COLUMN_MERCHANT_NAME = "name";

        public static final String COLUMN_MERCHANT_ADDRESS = "address";

        public static final String COLUMN_MERCHANT_PHONE = "phone";

        public static final String COLUMN_MERCHANT_LATITUDE = "latitude";

        public static final String COLUMN_MERCHANT_LONGITUDE = "Longitude";

    }

}
