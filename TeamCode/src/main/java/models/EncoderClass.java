package models;

public class EncoderClass {

    private int countsToCm;

    public void cmToCounts(double countsPerMotor, double driveGearReduction, double wheelDiameterCm, double countsPerCm, int countsToCm){
        countsPerMotor      = 288 ; // Contagens do motor qs
        driveGearReduction  = 2.0 ; // Redução?
        wheelDiameterCm     = 4.0 ; // Diametro da roda
        countsPerCm = (countsPerMotor * driveGearReduction) / (wheelDiameterCm * 3.1415);
        this.countsToCm = (int) Math.round(countsPerCm);
    }

    public int getCountsToCm() {
        return countsToCm;
    }
}
