package il.client.events;


public class RegisterEvent {
    boolean statusRegister;
    String result;

    public RegisterEvent(boolean statusRegister, String result) {
        this.statusRegister = statusRegister;
        this.result = result;
    }
    public boolean isStatusRegister() {
        return statusRegister;
    }

    public void setStatusRegister(boolean statusRegister) {
        this.statusRegister = statusRegister;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }




}
