package model;

/*Класс для одной0 клетки на шахматной доске*/
public class Cell {
	public enum CheckerColor {
		Black,
		White,
		NoColor;
	}
	public enum CheckerType {
		Pawn,
		King,
		NoChecker;
	}
	private CheckerType type;
	private CheckerColor color;
	
	private int horizontal = 0;
	private int vertical = 0;
	
	public Cell() {
		type = CheckerType.NoChecker;
		color = CheckerColor.NoColor;
	}
	
	public Cell(CheckerColor col){
		type = CheckerType.NoChecker;
		color = col;
	}
	
	public Cell(CheckerColor col, CheckerType currentType) {
		type = currentType;
		color = col;
	}
	
	public Cell(Cell cell) {
		type = cell.getType();
		color = cell.getColor();
		horizontal = cell.getHorizontal();
		vertical = cell.getVertical();
	}
	
	static public CheckerColor enemyColor(CheckerColor col) {
		if(col == CheckerColor.Black) return CheckerColor.White;
		else return CheckerColor.Black;
	}
	
	public Cell(int horizontal, int vertical, CheckerColor col, CheckerType currentType) {
		type = currentType;
		color = col;
		this.horizontal = horizontal;
		this.vertical = vertical;

	}
	
	public void setColor(CheckerColor col) {
		color = col;
	}
	
	public void setType(CheckerType ctype) {
		type = ctype;
	}
	
	public void setCoordinate(int horizontal, int vertical) {
		this.horizontal = horizontal;
		this.vertical = vertical;
	}
	
	public CheckerColor getColor() {
		return color;
	}
	
	public CheckerType getType() {
		return type;
	}
	
	public int getHorizontal() {
		return horizontal;
	}
	
	public int getVertical() {
		return vertical;
	}
	
	public boolean isBlackCell() {
		if((horizontal % 2) == (vertical % 2)) {
			return true;
		}
		return false;
	} 
	
	public String getStringCoordinate() {
		StringBuffer buf = new StringBuffer();
		buf.append(vertical);
		buf.append(horizontal);
		return buf.toString();
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if(color == CheckerColor.Black && type == CheckerType.Pawn) {
			buf.append('b');
		}
		else if(color == CheckerColor.Black && type == CheckerType.King) {
			buf.append('B');
		}
		else if(color == CheckerColor.White && type == CheckerType.Pawn) {
			buf.append('w');
		}
		else if(color == CheckerColor.White && type == CheckerType.King) {
			buf.append('W');
		}
		
		else if(isBlackCell()) {
			buf.append('#');
		}
		else {
			buf.append(' ');
		}
		
		return buf.toString();
	}
}
