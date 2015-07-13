/**
 * Copyright (C) 2008 J.F. Zarama
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frostwire.jlibtorrent.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

 /**
 * Offers an API to parse commands to a console application
 *
 * @author jf.zarama@gmail.com
 */
class ParseCmd {
    private final Map<String,Map<String,String>>  Parms;
    private final String                          help;
    private static final String pName    = "name";
    private static final String pValue   = "value";
    private static final String pRex     = "rex";
    private static final String pReq     = "req";
    private static final String pMsg     = "msg";
    private static final String pMon     = "mon";
    private static final String nRegEx   = "^([+-]{0,1})([0-9.]{1,})$";
    private static final String sRegEx   =
                                     //"^[^0-9]{1}([a-zA-Z0-9\\/\\_:\\.~]{1,})$";
                                       "^[^0-9]{1}([a-z@\\-!A-Z0-9\\/\\_:\\.~]{1,})$";  // change: 2011.09.18
    /**
     *
     */
    public static class Builder {
        private final Map<String,Map<String,String>>  Parms;
        private       String                          help;
        private       Map<String,String>              entryMap;

        /**
         * Builder offers a way to build a Map, Parms, that can later be
         * used by the enclosing class, ParseCmd.
         *
         * Each entry in Parms has a name, default value, regular expression,
         * flag indicating whether it is required or it is optional and a
         * message String used to display help/error as needed.
         *
         */
        public Builder() {
            Parms = new LinkedHashMap<String,Map<String,String>>();
            help  = "";
        }

        /**
         *
         * @param name
         * @param value
         * @param monadic
         * @return
         */
        public Builder parm(String name, String value, String monadic) {
            String ire = value.matches(nRegEx) ? nRegEx : sRegEx;//default regEx
            entryMap   = new LinkedHashMap<String,String>();// new Map for entry
            entryMap.put(pName,name);                   // parm name
            entryMap.put(pValue,value);                 // default value
            entryMap.put(pMon,monadic);                 // monadic flag
            entryMap.put(pRex,ire);                     // default regEx
            entryMap.put(pReq,"0");                     // default not required
            entryMap.put(pMsg,"");                      // message if error
            Parms.put(name,entryMap);                   // add it to Parms Map
            return this;
        }

        /**
         * Defines a parameter
         * @param name      parameter name
         * @param value     parameter default value
         * @return          reference to Builder; allows statement chaining
         */
        public Builder parm(String name, String value) {// standard name-value
            return parm(name,value,"0");                // monadic set to "0"
        }

        /**
         * defines a parameter without a default value; a monadic parameter
         * @param name      parameter name. Internally it is defined as
         *                  parm(name, "1")
         * @return          reference to Builder; allows statement chaining
         */
        public Builder parm(String name) {              // monadic parm
            parm(name,"1","1");                         // set value to "1"
            return this;                                // and flag  to "1"
        }

        /**
         *
         * @param h stores help/usage String to be used for usage information
         * @return  reference to this, Builder, so that chainning can be used
         */
        public Builder help(String h) {                 // define help String
            help = h;
            return this;
        }

        /**
         *
         * @param rex stores the regular-expression String for this arg
         * @return    reference to this, Builder, so that chainning can be used
         */
        public Builder rex(String rex) {                // define regExpression
            entryMap.put(pRex,rex);                     // default is set based
            return this;                                // on default value
        }

        /**
         *
         * @param req stores "0" or "1" String to flag whether it is required
         *            or optional; default is "0", optional
         * @return    reference to this, Builder, so that chainning can be used
         */
        public Builder req(String req) {                // required argument
            entryMap.put(pReq,req.matches("^[01]{1}$") ?// ensure "1" or "0"
                                             req : "1");// note: boolean
            return this;                                // is not used
        }

        /**
         *
         * @return      reference to this, Builder; enable statement chainning
         */
        public Builder req() {                          // required without
            return req("1");                            // String parm
        }

        /**
         *
         * @param desc stores error/help information to be displayed should
         *             regex fail to match
         * @return     reference to this, Builder, so that chainning can be used
         */
        public Builder msg(String desc) {               // define error message
            entryMap.put(pMsg,desc);                    // should parm fail
            return this;                                // rex test
        }

        /**
         *
         * @return instance of surrounding class passing Builder as argument
         */
        public ParseCmd build() {                       // return a CLI
            return new ParseCmd(this);                  // passing this Builder
        }
    }

    private ParseCmd(Builder builder) {                 // private constructor
        Parms = builder.Parms;                          // copy Parms Map
        help  = builder.help;                           // copy help String
    }

    /**
     *
     * @return defined help/usage String built by Builder
     */
    public String getHelp() {                           // return help String
        return this.help;
    }

    /**
     *
     * @param parmName defined parm-name as entered in Parms Map by Builder
     * @return         default value for parmName
     */
    public boolean isParm(String parmName) {            // is parm defined
        return Parms.containsKey(parmName);             // return
    }

    /**
     *
     * @param parmName defined parm-name as entered in Parms Map by Builder
     * @return         default value for parmName
     */
    public String getValue(String parmName) {           // get value for parm
        return getVars(parmName,pValue);
    }

    /**
     *
     * @param parmName  defined parm-name as enterd in Parms Map by Builder
     * @return          reg-expression String for parmName
     */
    public String getRex(String parmName) {             // get regEx for parm
        return getVars(parmName,pRex);
    }

    /**
     *
     * @param parmName  defined parm-name as enterd in Parms Map by Builder
     * @return          required flag, String "0" or "1" for parmName
     */
    public boolean isReq(String parmName) {             // get required
        String r = getVars(parmName,pReq);              // "0" or "1"
        return r.equals("1") ? true : false;            // return
    }

    /**
     *
     * @param parmName  defined parm-name as enterd in Parms Map by Builder
     * @return String value for error/help message for defined entry in Parms
     */
    public String getMsg(String parmName) {            // get emsg for parm
        return getVars(parmName,pMsg);                 // shown when rex fails
    }

    /**
     * Monadic term us used in predicate calculus to designate a form of logic
     * based on unary operators
     *
     * @param parmName  defined parm-name as enterd in Parms Map by Builder
     * @return          String value for error/help message for defined entry
     *                  in Parms
     */
    public boolean isMonadic(String parmName) {         // get monad for parm
        String m = getVars(parmName,pMon);              // m <- monadic value
        return  m.equals("1") ? true : false;           // return
    }

    /**
     *
     * @return size of Parms Map indicating number of defined parms
     */
    public int size() {                                 // number of parms
        return this.Parms.size();                       // defined
    }

    /**
     * Parses the args array looking for monads, arguments without value
     *              such as "-verbose" and if found inserts, forces,
     *              a value of "1" and returns the transformed args as a List
     *
     * @param args  Array of input values
     * @return      List of expanded name-value pair if monads are detected
     */

    private List<String> filterMonadics(String[] args) {// name-value for monads
        List<String> Y = new ArrayList<String>();       // Y <- return List
        for(String arg : args) {                        // iterate over args
            if(!isMonadic(arg)) {Y.add(arg);continue;}  // not monad: add it
            Y.add(arg);                                 // add monadic argument
            Y.add("1");                                 // insert a value to "1"
        }
        return Y;                                       // return List of args
    }

    /**
     * Validates args[] against regular expressions defined for each arg entry
     * in Parms Map
     *
     * @param  args
     * @return empty String if regex's pass or a message containing reason for
     *         failure. It includes also the help String as defined by Builder
     *         User should call validate() first; a non-emty String indicates
     *         parse error, and message is included. Should an empty String
     *         be the response, caller should invoke parse() which returns
     *         a merged Map using args[] and default settings in Parms
     */
    public String validate(String[] args) {             // validate args
        List<String> A = filterMonadics(args);          // detect monadics
        String required = validateRequired(A);          // required args
        if(required.length()>0) {                       // return if notEmpty
            return "\nenter required parms: " + required + "\n\n" + getHelp();
        }                                               // include getHelp()
        StringBuffer sb = new StringBuffer();           // check each arg
        String k,v,re;
        for(int i=0;i < A.size()-1;i +=2 ) {            // iterate over args
            k = A.get(i);                               // k <- arg
            v = A.get(i+1);                             // v <- value
            if(!isParm(k)){sb.append("\n" + k + " invalid "); break; }
            re = getRex(k);                             // get regEx
            if(!v.matches(re)) {                        // if no match add to sb
                sb.append( "\n" + k + " value of  '" + v + "' is invalid;\n");
                sb.append( "\t" + getMsg(k));           // append arg emsg
                break;
            }
        }
        if(sb.length() > 0) sb.append("\n\n\t" + getHelp() + "\n");
        return sb.toString();                           // ok: if emty String
    }

    public boolean isValid(String[] args) {
        String err =  this.validate(args);
        return err == null ? false : err.length() == 0;
    }

    /**
     *
     * @param args
     * @return
     */
    public Map<String,String> parse(String[] args) {    // merge args & defaults
        Map<String,String> R = new LinkedHashMap<String,String>();
        List<String> A = filterMonadics(args);          // detect and fill mons
        String k,v;
        for(int i=0;i<A.size()-1;i +=2) {
            k = A.get(i);
            v = (i+1) < A.size() ? A.get(i+1) : "";
            if(this.Parms.containsKey(k)) R.put(k, v);
        }
        for(Iterator p = this.Parms.keySet().iterator();p.hasNext();) {
            k = (String) p.next();
            v = getValue(k);
            if( !R.containsKey(k)) R.put(k, v);
        }
        return R;                                       // R parsed + defaults
    }

    private List<String> findRequired() {               // List <- required args
        String k;
        List<String> R = new ArrayList<String>();
        for(Iterator p = Parms.keySet().iterator(); p.hasNext();) { // iterate
            k = (String) p.next();                      // over Parms.ketSet()
            if(isReq(k)) R.add(k);                      // add it if required
        }
        return R;                                       // List of req'd args
    }

    private String validateRequired(List<String> X) {   // validate that all
        StringBuffer sb = new StringBuffer();           // required args
        List<String> R  = findRequired();               // are supplied
        int found = 0;
        for(String r : R) {
            for(String arg : X) if(r.equals(arg)) { found++; break; }
        }
        for(int i=0;i<R.size() && R.size() != found;i++) {  // where all req'd
            sb.append(R.get(i) + " ");                  // args found; if not
        }                                               // append req'd args
        return sb.toString().trim();                    // emty if all ok
    }                                                   // else show all req'd

    private String getVars(String parmName,String varName) {    // util to get
        Map<String,String> E;                                   // parm vars
        String r = "";                                          // such as:
        if(!this.Parms.containsKey(parmName)) return r;         //      value
        E = this.Parms.get(parmName);                           //      regex
        if(!E.containsKey(varName)) return r;                   //      req
        return E.get(varName);                                  //      emsg
    }                                                           //      monadic

    private static String fill(String sep,int n){   // private util for repeated
        StringBuffer sb = new StringBuffer();       // characters n using
        for(int i=0;i<n;i++) sb.append(sep);        // a separator String
        return sb.toString();
    }

    // methods below are not neded by CLI but used for testing under
    // public static void main(String[] args)

    private String displayR(Map<String, String> R) {    // used for testing
        StringBuffer sb = new StringBuffer();           // display Map
        String name,k;                                  // such as result of
        name = R.get("name");                           // parse process
        if(name != null) sb.append(name + ":" + "\n");
        for(Iterator r = R.keySet().iterator();r.hasNext();) {
            k = (String) r.next();
            if(k.equals("name")) continue;
            sb.append(fill(" ",20) + k + fill(" ",20-k.length())
                                                     + R.get(k));
            sb.append("\n");
        }
        return sb.toString();
    }

    private  String displayP(Map<String,Map<String,String>> M) {// for test
        StringBuffer sb = new StringBuffer("\n");       // to display
        String k;                                       // Map of Maps such as
        Map<String,String> E;                           // the Parms Map
        for(Iterator m = M.keySet().iterator();m.hasNext();) {
            k = (String) m.next();
            E = M.get(k);
            sb.append(displayR(E)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Convenience method to generate a String representing the values, parms,
     * produced by Buider and store in Parms
     *
     * @return      String sutable for printing by caller
     */
    public String displayParms() {
        return displayP(this.Parms);
    }

    /**
     * Convenience method for the caller to print the Map produced by parse()
     * @param M     A Map collections variable typically the output of
     *              parse(args)
     * @return      String representation of Map suitable for printing
     */
    public String displayMap(Map<String, String> M) {
        return displayR(M);
    }

    /**
     * main is included to show usage of the class to parse commands
     * for a console application.
     *
     * Three steps are required to use the ParseCmd
     *
     *      1. Define       new ParseCmd.Builder().help("usage .").parm()...
     *      2. Validate     String err = cmd.validate(args);
     *      3. Parse        if(err.isEmpty()) R = cmd.parse(args);
     *
     * @param args input args from command line
     */
    public static void main(String[] args) {

        String usage = "usage: -loop n  -delay nnn -ifile fileName [ -tt nn  -ofile abc ]";
        ParseCmd cmd = new Builder()
                      .help(usage)
                      .parm("-loop",    "10" ).req()
                      .parm("-delay",   "100").req()
                                              .rex("^[0-9]{3}$")
                                              .msg("must enter 3-digits.")
                      .parm("-ifile",   "java.txt").req()
                      .parm("-tt",      "0")
                      .parm("-ofile",   "readme.txt")
                      .parm("-verbose", "0").rex("^[01]{1}$")
                      .build();

        Map<String,String> R = new HashMap<String,String>();
        String parseError    = cmd.validate(args);
        if( cmd.isValid(args) ) {
            R = cmd.parse(args);
            System.out.println(cmd.displayMap(R));
        }
        else { System.out.println(parseError); System.exit(1); }
    }
    // R contains default or input values for defined parms and used as in:
    // long loopLimit = Long.parseLong( R.get("-loop"));
}
