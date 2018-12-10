package VendingMachineProject;

class Operator {
    private String type;
    private int code;

    Operator(String type, int code) {
        this.type = type;
        this.code = code;
    }

    int getCode() {
        return code;
    }

    String getCSV() {
        return type + "," + code;
    }
}