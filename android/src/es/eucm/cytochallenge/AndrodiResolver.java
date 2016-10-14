package es.eucm.cytochallenge;

import es.eucm.cytochallenge.utils.Resolver;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AndrodiResolver implements Resolver{

    @Override
    public String format(String format, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}
