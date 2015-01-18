package evaluation;


import org.apache.log4j.Logger;

public class ClassifyEvaluation {
	Logger logger = Logger.getLogger(ClassifyEvaluation.class);

	int classID;
	String className;
	int nt;
    int nf;
    int nft;
	
	double P;  //精度
	double R;  //召回率
	double F;  //F值
	
	public ClassifyEvaluation()
	{
		
	}
	
	public ClassifyEvaluation(int classID,int nt,int nf,int nft )
	{
		logger.info("nt:"+nt+" nf:"+nf+" nft:"+nft);
		this.classID = classID;
		this.nt = nt;
		this.nf = nf;
		this.nft = nft;
		className = "";
	}
	
	public void  calculate()
	{
		calculateP();
		calculateR();
		calculateF();
	}
	
	public void calculateP()
	{
		if(nf == 0)
		{
			P = 0.0;
		}
		else
		{
			P = (double)nft/(double)nf;
		}
		
		
	}
	
	public void calculateR()
	{
		if(nt == 0)
		{
			R = 0;
		}
		else
		{
			R = (double)nft/(double)nt;
		}
	}
	
	public void calculateF()
	{
		if(P == 0.0&&R == 0)
		{
			F = 0.0;
		}
		else
		{
			F = 2*P*R/(P+R);
		}
		
	}

	public int getClassID() {
		return classID;
	}

	public void setClassID(int classID) {
		this.classID = classID;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}	

	public double getP() {
		return P;
	}

	public double getR() {
		return R;
	}

	public double getF() {
		return F;
	}
	
	public String toString()
	{
		return className+" "+classID+" "+P+" "+R+" "+F;
	}

}
