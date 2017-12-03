package g28.sos1.eval;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EvaluationSeries {
    private final List<EvaluationInstance> evaluationInstances;

    public EvaluationSeries(List<EvaluationInstance> evaluationInstances) {
        this.evaluationInstances = evaluationInstances;
    }

    public List<EvaluationInstance> getEvaluationInstances() {
        return evaluationInstances;
    }

    public Result runAll() {
       return runAll(x -> {});
    }

    public Result runAll(Consumer<String> diagnosticConsumer) {
        List<EvaluationInstanceResult> list = new ArrayList<>();
        for (int i = 0; i < evaluationInstances.size(); i++) {
            diagnosticConsumer.accept(i + " / " + evaluationInstances.size());
            EvaluationInstance evaluationInstance = evaluationInstances.get(i);
            EvaluationInstanceResult result = evaluationInstance.execute();
            if (result != null) list.add(result);
        }
        return new Result(list);
    }

    public class Result {
        List<EvaluationInstanceResult> singleResults;

        public Result(List<EvaluationInstanceResult> singleResults) {
            this.singleResults = singleResults;
        }

        public List<EvaluationInstanceResult> getSingleResults() {
            return singleResults;
        }

        public Metric getDistance() {
            return getResultMetric(x -> x.getSolution().getDistance());
        }

        public Metric getDurations() {
            return getResultMetric(EvaluationInstanceResult::getDuration);
        }

        public Metric getResultMetric(Function<? super EvaluationInstanceResult, Long> resultMetric) {
            return new Metric(singleResults.stream().map(resultMetric).collect(Collectors.toList()));
        }

        public Metric getCount() {
            return getResultMetric(x -> (long) x.getSolution().getEdges().size());
        }

        @Override
        public String toString() {
            return "Duration: " + getDurations() + "\nTarget:   " + getDistance() + "\nCount:    " + getCount();
        }
    }
}
