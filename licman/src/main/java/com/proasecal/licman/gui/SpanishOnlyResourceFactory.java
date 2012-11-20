package com.proasecal.licman.gui;


import java.util.Properties;

import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.internationalization.java.ResourcesFactory;
import org.openswing.swing.util.java.Consts;



public class SpanishOnlyResourceFactory extends ResourcesFactory {

  /** internationalization settings */
  private Resources resources = null;


  /**
   * Constructor.
   * @param currencySymbol currency symbol
   * @param additionalDictionary additional descriptions
   * @param showResourceNotFoundWarning warn when no resource key not found
   */
  public SpanishOnlyResourceFactory(String currencySymbol,Properties additionalDictionary,boolean showResourceNotFoundWarning) {
    Properties dictionary = new Properties();

    dictionary.putAll(additionalDictionary);
    dictionary.setProperty(Consts.EQ,"Equals to");
    dictionary.setProperty(Consts.GE,"Greater or equals to");
    dictionary.setProperty(Consts.GT,"Greater than");
    dictionary.setProperty(Consts.IS_NOT_NULL,"Is filled");
    dictionary.setProperty(Consts.IS_NULL,"Is not filled");
    dictionary.setProperty(Consts.LE,"Less or equals to");
    dictionary.setProperty(Consts.LIKE,"Contains");
    dictionary.setProperty(Consts.LT,"Less than");
    dictionary.setProperty(Consts.NEQ,"Not equals to");
    dictionary.setProperty(Consts.IN,"Contains values");
    dictionary.setProperty(Consts.ASC_SORTED,"Ascending");
    dictionary.setProperty(Consts.DESC_SORTED,"Descending");
    dictionary.setProperty(Consts.NOT_IN,"Not contains values");

    resources = new Resources(
      dictionary,
      currencySymbol,
      '.',
      ',',
      Resources.YMD,
      true,
      '/',
      "HH:mm",
      "ES",
      showResourceNotFoundWarning
    );
  }


  /**
   * @return internationalization settings, according with the current language
   */
  public final Resources getResources() {
    return resources;
  }


  /**
   * Load dictionary, according to the specified language id.
   * @param langId language id identifier
   */
  public final void setLanguage(String langId) throws UnsupportedOperationException {
    if (!resources.getLanguageId().equals(langId))
      throw new UnsupportedOperationException("Identificador de lenguaje no soportado.");
  }


  /**
   * @param langId language id identifier
   * @return internationalization settings, according with the language specified
   */
  public final Resources getResources(String langId) throws UnsupportedOperationException {
    if (!resources.getLanguageId().equals(langId))
    throw new UnsupportedOperationException("Identificador de lenguaje no soportado.");
    return resources;
  }



}


