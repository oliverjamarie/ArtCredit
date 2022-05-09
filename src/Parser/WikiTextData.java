package Parser;

public class WikiTextData implements IWikiData {
    String data;

    public WikiTextData(String dataIN){
        data = dataIN;
    }

    @Override
    public void setData(String dataIN) {
        data = dataIN;
    }

    @Override
    public String getData() {
        return data;
    }
}
