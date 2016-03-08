package com.chen.remark.data;

/**
 * Created by chenfayong on 16/2/20.
 */
public class TableRemark {

    public interface RemarkColumns {

        /**
         * The unique Id for a row
         * <p>Type:Integer(long)</p>
         */
        public static final String REMARK_ID = "remark_id";

        /**
         * Created data for remark
         * <p>Type:Integer(long)</p>
         */
        public static final String CREATED_DATE = "created_date";

        /**
         * Latest modified date
         * <p>Type:Integer(long)</p>
         */
        public static final String MODIFIED_DATE = "modified_date";

        /**
         * Alert date
         * <p>Type:Integer(long)</p>
         */
        public static final String ALERTED_DATE = "alert_date";

        /**
         * Remark widget id
         * <p>Type:Integer(long)</p>
         */
        public static final String WIDGET_ID = "widget_id";

        /**
         * Remark widget type
         * <p>Type:Integer(long)</p>
         */
        public static final String WIDGET_TYPE = "widget_type";

        /**
         * Remark's background color's id
         * <p>Type:Integer(long)</p>
         */
        public static final String BG_COLOR_ID = "bg_color_id";

        /**
         * Remark's content
         * <p>Type:Text</p>
         */
        public static final String REMARK_CONTENT = "remark_content";

        /**
         * Text write mode
         */
        public static final String CHECK_LIST_MODE = "check_list_mode";
    }
}
