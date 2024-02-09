package ru.letsdigit.springaop;

import org.springframework.stereotype.Component;
import ru.letsdigit.springaop.aspect.RecoverException;
import ru.letsdigit.springaop.aspect.Timer;

import java.util.stream.IntStream;

@Component
public class ServiceBean {

    @Timer
    public void sortIt() {
        IntStream.range(0, 1_000_000).boxed().sorted();
    }

    /*
    * Console output:
    * 2024-02-09T23:25:17.031+07:00  INFO 14584 --- [           main] r.l.springaop.aspect.TimerAspect
    * : className: ServiceBean - methodName: sortIt, #288500 nanoseconds
    */

    // <- с ArithmeticException получаем Exception (при вызове делим на 0)
//    @RecoverException(noRecoverFor = {ArithmeticException.class})
    @RecoverException(noRecoverFor = {IllegalArgumentException.class}) // <- Здесь получаем null
    public void divisionByZero(int x) {
        System.out.println(1/x);
    }
}
