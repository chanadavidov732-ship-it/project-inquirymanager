package business;

import Shared.Inquiry;
import Shared.Representative;

public class InquiryHandlingTask implements Runnable{
    Inquiry inquiryToTreat;
    Representative treatingRepresentative;

    public InquiryHandlingTask() {}

    public InquiryHandlingTask(Representative treatingRepresentative, Inquiry inquiryToTreat) {
        this.treatingRepresentative = treatingRepresentative;
        this.inquiryToTreat = inquiryToTreat;
    }

    public Inquiry getInquiryToTreat() {
        return inquiryToTreat;
    }

    public Representative getTreatingRepresentative() {
        return treatingRepresentative;
    }

    public void setInquiryToTreat(Inquiry inquiryToTreat) {
        this.inquiryToTreat = inquiryToTreat;
    }

    public void setTreatingRepresentative(Representative treatingRepresentative) {
        this.treatingRepresentative = treatingRepresentative;
    }

    @Override
    public void run() {
        InquiryHandling inquiryHandling = new InquiryHandling(inquiryToTreat);
        inquiryHandling.start();
        try {
            inquiryHandling.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        inquiryToTreat.setStatus(Inquiry.Status.TREATED);
        inquiryToTreat.transferToHistory();
        InquiryManager.QRepresentative.add(treatingRepresentative);
    }
}
