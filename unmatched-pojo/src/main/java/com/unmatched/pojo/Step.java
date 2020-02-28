package com.unmatched.pojo;

public class Step {

    private double step_Id;

    private String stepName;

    private String assemblyName;

    private double stepIndex;

    private int run;

    private int skip;

    private int loop;

    private String procedure_Name;

    public double getStep_Id() {
        return step_Id;
    }

    public void setStep_Id(double step_Id) {
        this.step_Id = step_Id;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getAssemblyName() {
        return assemblyName;
    }

    public void setAssemblyName(String assemblyName) {
        this.assemblyName = assemblyName;
    }

    public double getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(double stepIndex) {
        this.stepIndex = stepIndex;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    public String getProcedure_Name() {
        return procedure_Name;
    }

    public void setProcedure_Name(String procedure_Name) {
        this.procedure_Name = procedure_Name;
    }

    @Override
    public String toString() {
        return "Step{" +
                "step_Id=" + step_Id +
                ", stepName='" + stepName + '\'' +
                ", assemblyName='" + assemblyName + '\'' +
                ", stepIndex=" + stepIndex +
                ", run=" + run +
                ", skip=" + skip +
                ", loop=" + loop +
                ", procedure_Name='" + procedure_Name + '\'' +
                '}';
    }
}
