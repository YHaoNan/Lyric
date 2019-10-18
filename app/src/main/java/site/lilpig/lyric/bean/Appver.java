package site.lilpig.lyric.bean;


public class Appver {
    private Integer id;
    private String ver;
    private Integer isforce;
    private String info;
    private String url;


    public Appver(){}
    public Appver(Integer id, String ver, Integer isforce, String info, String url) {
        this.id = id;
        this.ver = ver;
        this.isforce = isforce;
        this.info = info;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public Integer getIsforce() {
        return isforce;
    }

    public void setIsforce(Integer isforce) {
        this.isforce = isforce;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
