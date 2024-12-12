package procurmentsystem.Table;

public interface InteractionsWithTable {
    boolean add(String[] values);
    boolean delete();
    boolean update(String column, String oldValue, String newValue);
}
