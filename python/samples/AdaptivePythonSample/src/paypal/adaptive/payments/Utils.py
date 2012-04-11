'''
Created on Aug 8, 2010

@author: palavilli
'''
import urllib

def encodeParameter(params):
    l = []
    for k, v in params.iteritems():
        k = str(k)
        v = urllib.quote(str(v))
        l.append(k + '=' + v)
    return '&'.join(l)

def getValue(valueList):
    return valueList[0]