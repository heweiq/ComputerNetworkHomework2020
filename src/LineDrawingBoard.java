import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LineDrawingBoard extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("My Line Chart");
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");

        XYChart.Series<Number,Number> series1 = new XYChart.Series<>();
        series1.setName("Throughput");
        for(int i = 0; i < everySuc.length; i++)
            series1.getData().add(new XYChart.Data(i, everySuc[i]));

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series1);

        stage.setScene(scene);
        stage.show();
    }
    static int[] everySum, everySuc;
    public static void main(String[] args)
    {
        int n = 10, siz= 5;
        Prg5_2 prg5_2 = new Prg5_2(n,siz);
        Random random = new Random();
        Map<Pair<Integer,Integer>,Boolean> map = new HashMap<>();
        for(int i = 0; i < 20; i++)
        {
            int u, v;
            while(true)
            {
                u = random.nextInt(n) + 1;
                v = random.nextInt(n - 1) + 1;
                if (v == u) ++u;
                if(!map.containsKey(new Pair<>(u,v)))
                {
                    map.put(new Pair<>(u,v), true);
                    map.put(new Pair<>(v,u), true);
                    break;
                }
            }
            prg5_2.addEdge(u,v);
        }
        prg5_2.work(50);
        everySum = prg5_2.getEverySum();
        everySuc = prg5_2.getEverySuc();
        launch(args);
    }
}