package com.sf.module.esbinterface.fileutil;

public class ScheduleNode {
    private String zhrxh;
    private String pernr;
    private String begda;
    private String endda;
    private String beguz;
    private String enduz;
    private String vtken;
    private String tprog;
    private String zhrpbxt;
    private String zhrclbz;

    public ScheduleNode() {
    }

    public ScheduleNode(Long zhrxh, String pernr, String begda, String endda, String beguz, String enduz, String vtken, String tprog, String zhrpbxt) {
        this.zhrxh = String.valueOf(zhrxh);
    	this.pernr = pernr;
        this.begda = begda;
        this.endda = endda;
        this.beguz = beguz;
        this.enduz = enduz;
        this.vtken = vtken;
        this.tprog = tprog;
        this.zhrpbxt = zhrpbxt;
    }

    public String getZhrxh() {
        return zhrxh;
    }

    public void setZhrxh(String zhrxh) {
        this.zhrxh = zhrxh;
    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getBegda() {
        return begda;
    }

    public void setBegda(String begda) {
        this.begda = begda;
    }

    public String getEndda() {
        return endda;
    }

    public void setEndda(String endda) {
        this.endda = endda;
    }

    public String getBeguz() {
        return beguz;
    }

    public void setBeguz(String beguz) {
        this.beguz = beguz;
    }

    public String getEnduz() {
        return enduz;
    }

    public void setEnduz(String enduz) {
        this.enduz = enduz;
    }

    public String getVtken() {
        return vtken;
    }

    public void setVtken(String vtken) {
        this.vtken = vtken;
    }

    public String getTprog() {
        return tprog;
    }

    public void setTprog(String tprog) {
        this.tprog = tprog;
    }

    public String getZhrpbxt() {
        return zhrpbxt;
    }

    public void setZhrpbxt(String zhrpbxt) {
        this.zhrpbxt = zhrpbxt;
    }

    public String getZhrclbz() {
        return zhrclbz;
    }

    public void setZhrclbz(String zhrclbz) {
        this.zhrclbz = zhrclbz;
    }
}
