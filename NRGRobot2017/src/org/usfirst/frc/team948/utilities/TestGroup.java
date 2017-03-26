package org.usfirst.frc.team948.utilities;

import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class TestGroup implements LiveWindowSendable {
	private ITable m_table;
	
	@Override
	public void initTable(ITable subtable) {
		m_table = subtable;
		updateTable();
	}

	@Override
	public ITable getTable() {
		return m_table;
	}

	@Override
	public String getSmartDashboardType() {
		return "TestGroup";
	}

	@Override
	public void updateTable() {
		if (m_table != null)
		{
			m_table.putNumber("number", 123);
			m_table.putBoolean("bool", true);
			m_table.putString("string", "meeeme");
		}
	}

	@Override
	public void startLiveWindowMode() {
	}

	@Override
	public void stopLiveWindowMode() {
	}

}
