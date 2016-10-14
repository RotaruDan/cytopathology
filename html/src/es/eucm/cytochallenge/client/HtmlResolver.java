package es.eucm.cytochallenge.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DefaultDateTimeFormatInfo;
import es.eucm.cytochallenge.utils.Resolver;

import java.util.Date;


public class HtmlResolver implements Resolver {
    @Override
    public String format(String format, Date date) {
        DefaultDateTimeFormatInfo info = new DefaultDateTimeFormatInfo();
        DateTimeFormat dateFormat = new DateTimeFormat(format, info) {};

        return dateFormat.format(date);
    }
}
