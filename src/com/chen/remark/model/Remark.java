package com.chen.remark.model;

import android.content.ContentValues;
import com.chen.remark.data.TableRemark.RemarkColumns;

/**
 * Created by chenfayong on 16/2/20.
 */
public class Remark extends BaseBean {

    private  Integer remarkId = null;

    private Long createdDate = null;

    private Long modifiedDate = null;

    private Long alertDate = null;

    private Integer widgetId = null;

    private Integer widgetType = null;

    private Integer bgColorId = null;

    private String remarkContent = null;

    public Remark() {

    }

    public Remark(Integer widgetId, Integer widgetType, Integer bgColorId, String remarkContent) {
        this.createdDate = System.currentTimeMillis();
        this.modifiedDate = this.createdDate;
        this.alertDate = this.createdDate;
        this.widgetId = widgetId;
        this.widgetType = widgetType;
        this.bgColorId = bgColorId;
        this.remarkContent = remarkContent;
    }

    public Integer getRemarkId() {
        return remarkId;
    }

    public void setRemarkId(Integer remarkId) {
        this.remarkId = remarkId;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Long alertDate) {
        this.alertDate = alertDate;
    }

    public Integer getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(Integer widgetId) {
        this.widgetId = widgetId;
    }

    public Integer getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(Integer widgetType) {
        this.widgetType = widgetType;
    }

    public Integer getBgColorId() {
        return bgColorId;
    }

    public void setBgColorId(Integer bgColorId) {
        this.bgColorId = bgColorId;
    }

    public String getRemarkContent() {
        return remarkContent;
    }

    public void setRemarkContent(String remarkContent) {
        this.remarkContent = remarkContent;
    }


    public ContentValues transform2ContentValues() {
        ContentValues values = new ContentValues();
        this.addContentValue(values, RemarkColumns.REMARK_ID, this.remarkId);
        this.addContentValue(values, RemarkColumns.CREATED_DATE, this.createdDate);
        this.addContentValue(values, RemarkColumns.MODIFIED_DATE, this.modifiedDate);
        this.addContentValue(values, RemarkColumns.ALERTED_DATE, this.alertDate);
        this.addContentValue(values, RemarkColumns.WIDGET_ID, this.widgetId);
        this.addContentValue(values, RemarkColumns.WIDGET_TYPE, this.widgetType);
        this.addContentValue(values, RemarkColumns.BG_COLOR_ID, this.bgColorId);
        this.addContentValue(values, RemarkColumns.REMARK_CONTENT, this.remarkContent);

        return values;
    }
}
