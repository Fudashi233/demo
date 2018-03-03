@Table("student")
public class StudentFilter
{
	@Column(name = "sId",type = "INT")
   private int id;
	
	@Column(name = "sName",type = "VARCHAR")
   private String name;

	@Column(name = "",sDepartmentId")
   private int departmentId;
	
	@Column("sClass")
   private String classNum;
	
	@Column("sSex")
   private String sex;
	
	public StudentFilter()
	{
		
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setDepartmentId(int departmentId)
	{
		this.departmentId = departmentId;
	}
	public void setClassNum(String classNum)
	{
		this.classNum = classNum;
	}
	public void setSex(String sex)
	{
		this.sex = sex;
	}
	public int getId()
	{
		return this.id;
	}
	public String getName()
	{
		return this.name;
	}
	public int getDepartmentId()
	{
		return this.departmentId;
	}
	public String getClassNum()
	{
		return this.classNum;
	}
	public String getSex()
	{
       return this.sex;		
	}
}



