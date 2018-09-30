package getdata.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.crab2died.converter.ReadConvertible;
import com.github.crab2died.converter.WriteConvertible;

public class IncludeCammaCell2List implements ReadConvertible{

	@Override
	public Object execRead(String object) {
		return new ArrayList<String>(
				Arrays.asList(object.split(","))
				);
	}

}
