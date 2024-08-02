package Questions;

public class Question {
    private int id;
    private String text;
    private String a;
    private String b;
    private String c;
    private String d;
    private String correct;

    public Question(int id, String text, String q1, String q2, String q3, String q4, String correct){
        this.id = id;
        this.text = text;
        this.a = q1;
        this.b = q2;
        this.c = q3;
        this.d = q4;
        this.correct = correct;
    }

    public int getId(){
        return this.id;
    }

    public String getText(){
        return this.text;
    }

    public String getA(){
        return this.a;
    }

    public String getB(){
        return this.b;
    }

    public String getC(){
        return this.c;
    }

    public String getD(){
        return this.d;
    }

    public String getCorrect(){
        return this.correct;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setText(String text){
        this.text = text;
    }

    public void setA(String a){
        this.a = a;
    }

    public void setB(String q){
        this.b = b;
    }

    public void setC(String c){
        this.c = c;
    }

    public void setD(String d){
        this.d = d;
    }

    public void setCorrect(String correct){
        this.correct = correct;
    }

    @Override
    public String toString(){
        return "Questions.Question " + id + ": " + text + "\n" + "a) " + a + " b) " + b + "\n" + "c) " + c + " d) " + d;
    }
}
