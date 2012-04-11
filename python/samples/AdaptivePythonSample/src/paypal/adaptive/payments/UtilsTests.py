'''
Created on Aug 15, 2010

@author: palavilli
'''
import unittest
import Utils

class TestUtilsFucntions(unittest.TestCase):


    def testEncodeParameter(self):
        param = dict()
        param['test1'] = 'value1'
        param['test2.key'] = 'value2.val'
        param['test3(0)'] = 'value3'
        
        encodedParam = Utils.encodeParameter(param)
        
        pass


if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()