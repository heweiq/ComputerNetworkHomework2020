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
        xAxis0.setLabel("Time");
        final LineChart<Number,Number> lineChart0 = new LineChart<>(xAxis0,yAxis0);
        lineChart0.setTitle("1");

        XYChart.Series<Number,Number> series1 = new XYChart.Series<>();
        series1.setName("Throughput");
        for(int i = 0; i < everySuc.length; i += 10)
        {
            int y = everySuc[i + 9];
            if(i != 0) y -= everySuc[i - 1];
            series1.getData().add(new XYChart.Data(i, y));
        }

        NumberAxis yAxis1 = new NumberAxis();
        final LineChart<Number,Number> lineChart1 = new LineChart<>(xAxis0,yAxis1);
        lineChart1.setTitle("2");
        XYChart.Series<Number,Number> series2 = new XYChart.Series<>();
        series2.setName("Error rate");
        for(int i = 0; i < everySuc.length; i += 10)
        {
            double y1 = everySuc[i + 9];
            if(i != 0) y1 -= everySuc[i - 1];
            double y2 = everySum[i + 9];
            if(i != 0) y2 -= everySum[i - 1];
            double y = 1 - (y1 / y2);
            series2.getData().add(new XYChart.Data(i, y));
        }

        VBox vBox = new VBox();
        vBox.getChildren().addAll(lineChart0,lineChart1);
        Scene scene  = new Scene(vBox,800,800);
        lineChart0.getData().add(series1);
        lineChart1.getData().add(series2);

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
        prg5_2.work(200);
        everySum = prg5_2.getEverySum();
        everySuc = prg5_2.getEverySuc();
        launch(args);
    }
}