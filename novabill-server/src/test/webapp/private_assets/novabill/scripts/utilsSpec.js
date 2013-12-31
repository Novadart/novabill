'use strict';


describe('novabill.utils', function() {
	
	beforeEach( module('novabill.utils') );

	describe('nSorting', function() {
		
		it('should sort clients ascending', inject(function( nSorting ) {
			
			var clients = [{name : 'Carl Bass'}, {name : 'Zack Snyder'}, {name : 'Robert Cuzman'}];
			
			clients.sort(nSorting.clientsComparator);
			
			expect(clients[0].name).toEqual('Carl Bass');
			expect(clients[2].name).toEqual('Zack Snyder');
		}));
		
		
//		it('should sort price lits ascending', inject(function( nSorting ) {
//			
//			var priceLists = [{name : 'Spring Beauty'}, {name : 'Winter Selection'}, {name : 'Fancy Items'}];
//			
//			priceLists.sort(nSorting.priceListsComparator);
//			
//			expect(priceLists[0].name).toEqual('Fancy Items');
//			expect(priceLists[2].name).toEqual('Winter Selection');
//		}));
		
		
	});

});
