package sendyou.biz.senduandroid.data;

/**
 * Created by pyh42 on 2017-01-01.
 */

public class Address {
    private String time;

    private String sort;

    private AddressResult[] results;

    private String cache;

    private String count;

    private String error;

    private String nums;

    private String type;

    private String lang;

    private String msg;

    private String version;

    public String getTime ()
    {
        return time;
    }

    public void setTime (String time)
    {
        this.time = time;
    }

    public String getSort ()
    {
        return sort;
    }

    public void setSort (String sort)
    {
        this.sort = sort;
    }

    public AddressResult[] getResults ()
    {
        return results;
    }

    public void setResults (AddressResult[] results)
    {
        this.results = results;
    }

    public String getCache ()
    {
        return cache;
    }

    public void setCache (String cache)
    {
        this.cache = cache;
    }

    public String getCount ()
    {
        return count;
    }

    public void setCount (String count)
    {
        this.count = count;
    }

    public String getError ()
    {
        return error;
    }

    public void setError (String error)
    {
        this.error = error;
    }

    public String getNums ()
    {
        return nums;
    }

    public void setNums (String nums)
    {
        this.nums = nums;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getLang ()
    {
        return lang;
    }

    public void setLang (String lang)
    {
        this.lang = lang;
    }

    public String getMsg ()
    {
        return msg;
    }

    public void setMsg (String msg)
    {
        this.msg = msg;
    }

    public String getVersion ()
    {
        return version;
    }

    public void setVersion (String version)
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [time = "+time+", sort = "+sort+", results = "+results+", cache = "+cache+", count = "+count+", error = "+error+", nums = "+nums+", type = "+type+", lang = "+lang+", msg = "+msg+", version = "+version+"]";
    }
}
