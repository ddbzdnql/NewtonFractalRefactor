public class Complex {
    double real;
    double imag;

    protected Complex(double x, double y){
        real = x;
        imag = y;
    }

    protected double measure(){
        return Math.sqrt((Math.pow(real, 2) + Math.pow(imag, 2)));
    }

    protected Complex times(Complex other){
        return new Complex(real*other.real - imag * other.imag, real*other.imag + imag*other.real);
    }

    protected Complex pow(int exp){
        Complex toRet = new Complex(1, 0);
        for (int i = 0; i < exp; i++){
            toRet = toRet.times(this);
        }
        return toRet;
    }

    protected Complex mult(double coef){
        return new Complex(real * coef, imag * coef);
    }

    protected Complex add(Complex other){
        return new Complex(real + other.real, imag + other.imag);
    }

    protected Complex minus(Complex other){
        return new Complex(real - other.real, imag - other.imag);
    }

    protected Complex divideBy(Complex other){
        Complex conjugate = new Complex(other.real, -other.imag);
        Complex up = this.times(conjugate);
        Complex bottom = other.times(conjugate);
        return new Complex(up.real/bottom.real, up.imag/bottom.real);
    }
}
