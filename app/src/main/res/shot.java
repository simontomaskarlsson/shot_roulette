/**
 * Created by Simon on 2017-02-01.
 */
public class shot {
    private static shot ourInstance = new shot();

    public static shot getInstance() {
        return ourInstance;
    }

    private shot() {
    }
}
