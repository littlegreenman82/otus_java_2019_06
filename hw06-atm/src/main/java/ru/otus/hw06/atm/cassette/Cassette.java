package ru.otus.hw06.atm.cassette;

public class Cassette implements CassetteInterface{

    private final FaceValue faceValue;

    private int count;

    public Cassette(FaceValue faceValue) {
        this.faceValue = faceValue;
    }

    @Override
    public FaceValue getFaceValue() {
        return faceValue;
    }

    @Override
    public void add(int faceValueCount) {
        this.count += faceValueCount;
    }

    @Override
    public void remove(int count) {
        if (this.count != 0)
            this.count -= count;

    }

    @Override
    public int getTotalAmount() {
        return faceValue.getValue() * count;
    }

    @Override
    public int withdraw(int amount) {

        if (amount >= faceValue.getValue()) {
            int neededBanknotesCount = (int)Math.floor(amount / faceValue.getValue());

            int returnCount;
            if (neededBanknotesCount > count) {
                returnCount = count;
            } else {
                returnCount = neededBanknotesCount;
            }

            remove(returnCount);
            return returnCount;
        }

        return 0;
    }

}
