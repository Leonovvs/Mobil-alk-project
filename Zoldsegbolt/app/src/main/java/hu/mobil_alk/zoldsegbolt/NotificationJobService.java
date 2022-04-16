package hu.mobil_alk.zoldsegbolt;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        new NotificationHandler(getApplicationContext())
            .send("Fontos az egészség", "Vásároljon friss zsölgség és gyümülcs kínálatunkból", R.drawable.grocery_icon, 0);

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
