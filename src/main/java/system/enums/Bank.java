package system.enums;

/**
 * Enum в котором будем хранить информацию о сторонних банках
 */
public enum Bank {
    FIRST_BANK("http://localhost:8080/", 200);

    private String url;
    private int bic;
    Bank(String url, int bic)
    {
        this.url = url;
        this.bic = bic;
    }
    public String getUrl() {
        return this.url;
    }
    public int getBic() {
        return this.bic;
    }

    public Bank getBankByBic(int bic)
    {
        for (Bank b : Bank.values())
        {
            if (b.getBic() == bic) return b;
        }
        throw new IllegalArgumentException();
    }

    public static boolean existBankByBic(int bic)
    {
        for (Bank b : Bank.values())
        {
            if (b.getBic() == bic) return true;
        }
       return false;
    }
}
