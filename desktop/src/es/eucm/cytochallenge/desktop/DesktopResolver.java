package es.eucm.cytochallenge.desktop;

import es.eucm.cytochallenge.utils.Resolver;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DesktopResolver implements Resolver{

    @Override
    public String format(String format, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}
