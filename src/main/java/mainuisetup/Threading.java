package mainuisetup;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Threading {
    public static final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);

    protected static void setupPerformanceMode() {
        Menus.performanceGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == Menus.efficiencyMode) {
                setThreadPoolSize(2);
            } else if (newValue == Menus.defaultMode) {
                setThreadPoolSize(4);
            } else if (newValue == Menus.performanceMode) {
                setThreadPoolSize(8);
            }
        });
    }

    private static void setThreadPoolSize(int size) {
        if (size > executorService.getCorePoolSize()) {
            executorService.setMaximumPoolSize(size);
            executorService.setCorePoolSize(size);
        } else {
            executorService.setCorePoolSize(size);
            executorService.setMaximumPoolSize(size);
        }
    }
}