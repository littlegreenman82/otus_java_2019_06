package ru.otus.hw07.atm.cassette;

public class CassetteImpl implements Cassette {

    private final FaceValue faceValue;

    private int count;

    public CassetteImpl(FaceValue faceValue) {
        this.faceValue = faceValue;
    }

    @Override
    public FaceValue getFaceValue() {
        return faceValue;
    }

    @Override
    public void add(int banknotesCount) {
        this.count += banknotesCount;
    }

    @Override
    public void remove(int banknotesCount) {
        if (this.count != 0)
            this.count -= banknotesCount;

    }

    @Override
    public int balance() {
        return faceValue.getValue() * count;
    }

    @Override
    public int acceptableWithdrawal(int amount) {
        int returnCount = 0;

        if (amount >= faceValue.getValue()) {
            int neededBanknotesCount = amount / faceValue.getValue();

            if (neededBanknotesCount > count) {
                returnCount = count;
            } else {
                returnCount = neededBanknotesCount;
            }
        }

        return returnCount;
    }

    @Override
    public int withdraw(int amount) {
        int returnCount = acceptableWithdrawal(amount);

        remove(returnCount);
        return returnCount;
    }

}
