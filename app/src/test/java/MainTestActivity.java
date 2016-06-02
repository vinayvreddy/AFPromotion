import com.promotion.afpromotion.BuildConfig;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants=BuildConfig.class, sdk= 23)

public class MainTestActivity {

    @Before
    public void setUp() throws Exception {

    }
}