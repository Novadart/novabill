'''
Created on Oct 21, 2012

@author: risto
'''
import random
from gen_sql_script import gen_sql_script

if __name__ == '__main__':
    numInvItems = [random.randint(1, 50) for i in range(200)]
    gen_sql_script("/home/risto/Desktop/gen.sql", 1, 100, 200, numInvItems, 4, 1)