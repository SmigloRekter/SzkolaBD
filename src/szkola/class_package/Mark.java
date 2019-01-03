package szkola.class_package;
//Klasa opisuj¹ca ocene
public class Mark {

	private Integer mark;
	private Integer teacherID;
	private Integer courseID;
	private Integer studentID;
	public Integer getMark() {
		return mark;
	}
	public void setMark(Integer mark) {
		this.mark = mark;
	}
	public Integer getTeacherID() {
		return teacherID;
	}
	public void setTeacherID(Integer teacherID) {
		this.teacherID = teacherID;
	}
	public Integer getCourseID() {
		return courseID;
	}
	public void setCourseID(Integer courseID) {
		this.courseID = courseID;
	}
	public Integer getStudentID() {
		return studentID;
	}
	public void setStudentID(Integer studentID) {
		this.studentID = studentID;
	}
}
