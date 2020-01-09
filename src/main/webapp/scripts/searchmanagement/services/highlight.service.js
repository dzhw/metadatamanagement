// Original JavaScript code by Chirp Internet: www.chirp.com.au
// Please acknowledge use of this code by including this header.
/* global _ */
'use strict';

angular.module('metadatamanagementApp').factory(
  'HighlightService',
  function($document) {
    var hiliteTag = 'EM';
    var skipTags = new RegExp('^(?:' + hiliteTag + ')$');

    // characters to strip from start and end of the input string
    var endRegExp = new RegExp('^[^\\w]+|[^\\w]+$', 'g');

    // characters used to break up the input string into words
    var breakRegExp = new RegExp('[^\\w\'-]+', 'g');

    var openLeft = false;
    var openRight = false;
    var getRegex = function(text) {
      text = text.replace(/\\u[0-9A-F]{4}/g, ''); // remove missed unicode
      text = text.replace(endRegExp, '');
      text = text.replace(breakRegExp, '|');
      text = text.replace(/^\||\|$/g, '');
      text = addAccents(text);
      if (text) {
        var re = '(' + text + ')';
        if (openLeft) {
          re = '\\b' + re;
        }
        if (openRight) {
          re = re + '\\b';
        }
        var matchRegExp = new RegExp(re, 'i');
        return matchRegExp;
      }
      return false;
    };

    var highlightWords = function(node, matchRegExp, skipClasses) {
      if (!node) {
        return;
      }
      if (!matchRegExp) {
        return;
      }
      if (skipTags.test(node.nodeName)) {
        return;
      }
      if (skipClasses && skipClasses.length) {
        if (_.intersection(skipClasses, node.classList).length > 0) {
          return;
        }
      }

      if (node.hasChildNodes()) {
        for (var i = 0; i < node.childNodes.length; i++) {
          highlightWords(node.childNodes[i], matchRegExp, skipClasses);
        }
      }
      if (node.nodeType === 3) { // NODE_TEXT
        var nv;
        var regs;
        if ((nv = node.nodeValue) && (regs = matchRegExp.exec(nv))) {
          var match = $document[0].createElement(hiliteTag);
          match.appendChild($document[0].createTextNode(regs[0]));

          var after = node.splitText(regs.index);
          after.nodeValue = after.nodeValue.substring(regs[0].length);
          node.parentNode.insertBefore(match, after);
        }
      }
    };

    // convert escaped UNICODE to ASCII
    function removeUnicode(input) {
      var retval = input;
      retval = retval.replace(/\\u(00E[024]|010[23]|00C2)/ig, 'a');
      retval = retval.replace(/\\u00E7/ig, 'c');
      retval = retval.replace(/\\u00E[89AB]/ig, 'e');
      retval = retval.replace(/\\u(00E[EF]|00CE)/ig, 'i');
      retval = retval.replace(/\\u00F[46]/ig, 'o');
      retval = retval.replace(/\\u00F[9BC]/ig, 'u');
      retval = retval.replace(/\\u00FF/ig, 'y');
      retval = retval.replace(/\\u(00DF|021[89])/ig, 's');
      retval = retval.replace(/\\u(0163i|021[AB])/ig, 't');
      return retval;
    }

    // convert ASCII to wildcard
    function addAccents(input) {
      var retval = input;
      retval = retval.replace(/([ao])e/ig, '$1');
      retval = retval.replace(/ss/ig, 's');
      retval = retval.replace(/e/ig, '[eÃ¨Ã©ÃªÃ«]');
      retval = retval.replace(/c/ig, '[cÃ§]');
      retval = retval.replace(/i/ig, '[iÃ®Ã¯]');
      retval = retval.replace(/u/ig, '[uÃ¹Ã»Ã¼]');
      retval = retval.replace(/y/ig, '[yÃ¿]');
      retval = retval.replace(/s/ig, '(ss|[sÃŸÈ™])');
      retval = retval.replace(/t/ig, '([tÅ£È›])');
      retval = retval.replace(/a/ig, '([aÃÃ¢Ã¤Äƒ]|ae)');
      retval = retval.replace(/o/ig, '([oÃ´Ã¶]|oe)');
      return retval;
    }

    // added by Yanosh Kunsh to include utf-8 string comparison
    function dec2hex4(textString) {
      /* jshint -W016 */
      var hexequiv = new Array('0', '1', '2', '3', '4', '5', '6', '7', '8',
        '9', 'A', 'B', 'C', 'D', 'E', 'F');
      return hexequiv[(textString >> 12) & 0xF] +
        hexequiv[(textString >> 8) & 0xF] +
        hexequiv[(textString >> 4) & 0xF] + hexequiv[textString & 0xF];
    }

    // escape UNICODE characters in string
    function escapeUnicode(str) {
      /* jshint -W016 */
      // convertCharStr2jEsc
      // Converts a string of characters to JavaScript escapes
      // str: sequence of Unicode characters
      var highsurrogate = 0;
      var suppCP;
      var pad;
      var outputString = '';
      for (var i = 0; i < str.length; i++) {
        var cc = str.charCodeAt(i);
        if (cc < 0 || cc > 0xFFFF) {
          outputString += '!Error in convertCharStr2UTF16: unexpected' +
            ' charCodeAt result, cc=' + cc + '!';
        }
        if (highsurrogate !== 0) {
          // this is a supp char, and cc contains the low surrogate
          if (0xDC00 <= cc && cc <= 0xDFFF) {
            suppCP = 0x10000 + ((highsurrogate - 0xD800) << 10) + (cc - 0xDC00);
            suppCP -= 0x10000;
            outputString += '\\u' + dec2hex4(0xD800 | (suppCP >> 10)) + '\\u' +
              dec2hex4(0xDC00 | (suppCP & 0x3FF));
            highsurrogate = 0;
            continue;
          } else {
            outputString += 'Error in convertCharStr2UTF16: low surrogate ' +
              'expected, cc=' + cc + '!';
            highsurrogate = 0;
          }
        }
        if (0xD800 <= cc && cc <= 0xDBFF) { // start of supplementary character
          highsurrogate = cc;
        } else { // this is a BMP character
          switch (cc) {
            case 0:
              outputString += '\\0';
              break;
            case 8:
              outputString += '\\b';
              break;
            case 9:
              outputString += '\\t';
              break;
            case 10:
              outputString += '\\n';
              break;
            case 13:
              outputString += '\\r';
              break;
            case 11:
              outputString += '\\v';
              break;
            case 12:
              outputString += '\\f';
              break;
            case 34:
              outputString += '\\\"';
              break;
            case 92:
              outputString += '\\\\';
              break;
            default:
              if (cc > 0x1f && cc < 0x7F) {
                outputString += String.fromCharCode(cc);
              } else {
                pad = cc.toString(16).toUpperCase();
                while (pad.length < 4) {
                  pad = '0' + pad;
                }
                outputString += '\\u' + pad;
              }
          }
        }
      }
      return outputString;
    }

    var apply = function(element, text, skipClasses) {
      if (text === undefined || !(text = text.replace(/(^\s+|\s+$)/g, ''))) {
        return;
      }
      text = escapeUnicode(text);
      text = removeUnicode(text);
      highlightWords(element, getRegex(text), skipClasses);
    };
    return {
      apply: apply
    };
  }
);
