package org.firstinspires.ftc.teamcode;

public class EncoderConverter {

    /*Welcome, this is the team 10641(Hydra) code for convert centimeterLinear in encoderCount, first applied in the
     *Rover Ruckus season.
     *First of all, we declare 3 floats attributes, corresponding to: wheel diameter, gear ratio and the distance
     * between wheels. They will be used in the calculations of this class.*/

    public float wheelDiameter, gearRatio, distanceBetweenWheels;

    EncoderConverter( float wheelDiameter, float gearRatio, float distanceBetweenWheels){
        this.wheelDiameter = wheelDiameter;
        this.gearRatio = gearRatio;
        this.distanceBetweenWheels = distanceBetweenWheels;
    }

    /* LINEAR CONVERTER DESCRIPTION:
     *
     * The encoder of the "REV core hex motor" has the fractional rotation in 288 parts. so if we want a rotation,
     * we have to multiply it by 288:
     *
     * converted value = rotation * 288
     *
     * But, before we pass the amount of rotation, we have to convert the distance in centimeters to rotations.
     * To do this, it's simple, we will divide the desired distance by the circumference of the wheel and multiply by
     * the ratio of gears:
     *
     * rotation = distance desired * distance between wheels (diameter) * PI * gear ratio.
     */

    public int centimeterLinear(double cmDistance){
        double convertedValue, rotation;
        rotation = cmDistance/(this.wheelDiameter * this.gearRatio * Math.PI);
        convertedValue = rotation * 288;
        return ((int)Math.round(convertedValue));
    }

    /* ANGULAR CONVERTER DESCRIPTION:
     *
     * The definition of rotation in this mode is different, since we will convert an arc to rotations,
     * we will take into account the angle (ATTENTION: this converter is only for curves in the axis of rotation of the robot).
     * However, the definition of converted value remains the same.
     * This time we will use the principle of proportion. We will compare the ratio between the requested angle and the angle
     * of a circle (360°), with the ratio of the desired arc to the circumference of the robot (we will write a circle with the
     * distance between wheels as diameter). So it will look like this:
     *
     * angle X/360 = arc desired/ robot circumference.
     *
     * How we want to convert the arc to rotations:
     *
     * arc desired = robot circunference * angle X / 360.
     *
     * Deriving to fit our attributes, and add the gear ratio:
     *
     * arc desired = (distance between wheel * PI ) * gear ratio * angle X/ 360.
     */

    public int centimeterAngular(double angle){
        return (centimeterLinear((angle/360)*distanceBetweenWheels*Math.PI));
    }

    /* ANGULAR CONVERTER DESCRIPTION:
     *
     * This method is applied to curves with one motor only. Since the only thing in the calculation that is changed is the size
     * of the radius (which is twice as large), we will use the previous method by doubling the value.
     */

    public int centimeterAngularEx(double angle){
        return (centimeterAngular(angle) * 2);
    }
}
