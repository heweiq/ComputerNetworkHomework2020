import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LineDrawingBoard extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("My Line Chart");
        NumberAxis xAxis0 = new NumberAxis();
        NumberAxis yAxis0 = new NumberAxis();
        xAxis0.setLabel("Interval");
        final LineChart<Number,Number> lineChart0 = new LineChart<>(xAxis0,yAxis0);
        lineChart0.setTitle("1");
        XYChart.Series<Number,Number> series1 = new XYChart.Series<>();
        series1.setName("Throughput");

        NumberAxis yAxis1 = new NumberAxis();
        final LineChart<Number,Number> lineChart1 = new LineChart<>(xAxis0,yAxis1);
        lineChart1.setTitle("2");
        XYChart.Series<Number,Number> series2 = new XYChart.Series<>();
        series2.setName("Error rate");

        for(int t = 1; t <= 100; t++)
        {
            int n = 100, siz = 50;
            prg5_2 = new Prg5_2(n,siz);
            Random random = new Random();
            Map<Pair<Integer,Integer>,Boolean> map = new HashMap<>();
            for(int i = 0; i < 800; i++)
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
            prg5_2.Floyed();
            prg5_2.work(100, t);
            sum = prg5_2.getSum();
            suc = prg5_2.getSuc();
            err = prg5_2.getErr();
            series1.getData().add(new XYChart.Data(t, suc));
            series2.getData().add(new XYChart.Data(t, (double)err / sum));
        }

        VBox vBox = new VBox();
        vBox.getChildren().addAll(lineChart0,lineChart1);
        Scene scene  = new Scene(vBox,800,800);
        lineChart0.getData().add(series1);
        lineChart1.getData().add(series2);

        stage.setScene(scene);
        stage.show();
    }
    static Prg5_2 prg5_2;
    static int sum, suc, err;
    public static void main(String[] args)
    {
        launch(args);
    }
}