package szkola.class_package;

import java.sql.Date;
//Klasa opisuj¹ca zajêcia
public class Course {

	private Integer duration;
	private String name;
	private String date;
	private Integer teacherID;
	private Integer classroomID;
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getTeacherID() {
		return teacherID;
	}
	public void setTeacherID(Integer teacherID) {
		this.teacherID = teacherID;
	}
	public Integer getClassroomID() {
		return classroomID;
	}
	public void setClassroomID(Integer classroomID) {
		this.classroomID = classroomID;
	}
	
	
	
	
}
