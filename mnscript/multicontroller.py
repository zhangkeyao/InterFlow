#!/usr/bin/python

"""
This example creates a multi-controller network from semi-scratch by
using the net.add*() API and manually starting the switches and controllers.

This is the "mid-level" API, which is an alternative to the "high-level"
Topo() API which supports parametrized topology classes.

Note that one could also create a custom switch class and pass it into
the Mininet() constructor.
"""

from mininet.net import Mininet
from mininet.node import RemoteController, OVSSwitch
from mininet.link import Link, TCLink
from mininet.cli import CLI
from mininet.log import setLogLevel

def multiControllerNet():
    "Create a network from semi-scratch with multiple controllers."
    pre = 'config'

    class MultiSwitch( OVSSwitch ):
       "Custom Switch() subclass that connects to different controllers"
       def start( self, controllers ):
          return OVSSwitch.start( self, [ cmap[ self.name ] ] )
    net = Mininet( controller=RemoteController, switch=MultiSwitch , link=TCLink , autoSetMacs=True )

    print "*** Creating (reference) controllers"
    c1 = net.addController( 'c1', port=6653, ip='101.6.30.182' )
    c2 = net.addController( 'c2', port=6653, ip='101.6.30.183' )
    c3 = net.addController( 'c3', port=6653, ip='101.6.30.184' )
    c4 = net.addController( 'c4', port=6653, ip='101.6.30.185' )
    c5 = net.addController( 'c5', port=6653, ip='101.6.30.186' )

    print "*** Creating switches"
    s1  = net.addSwitch( 's1', protocols="OpenFlow10" )
    s2  = net.addSwitch( 's2', protocols="OpenFlow10" )
    s3  = net.addSwitch( 's3', protocols="OpenFlow10" )
    s4  = net.addSwitch( 's4', protocols="OpenFlow10" )
    s5  = net.addSwitch( 's5', protocols="OpenFlow10" )

    print "*** Creating hosts"
    h1 = net.addHost( 'h1' )
    h2 = net.addHost( 'h2' )
    h3 = net.addHost( 'h3' )
    h4 = net.addHost( 'h4' )
    h5 = net.addHost( 'h5' )

    print "*** Creating links in Subnets"

    print "*** Creating links between Subnets"
    net.addLink( s1, s2 )
    net.addLink( s2, s3 )
    net.addLink( s3, s4 )
    net.addLink( s4, s5 )
    net.addLink( s5, s1 )


    print "*** Creating links between hosts and switches"

    net.addLink( h1, s1 )
    net.addLink( h2, s2 )
    net.addLink( h3, s3 )
    net.addLink( h4, s4 )
    net.addLink( h5, s5 )

    print "*** Create Controller-Switch Map"
    cmap = { 's1': c1, 's2': c2,
    		 's3': c3, 's4': c4,
    		 's5': c5 }

    print "*** Starting network"
    net.build()
    CLI( net, script = pre )

    net.start()
    

    print "*** Running CLI"
    CLI( net )

    print "*** Stopping network"
    net.stop()

if __name__ == '__main__':
    setLogLevel( 'info' )  # for CLI output
    multiControllerNet()

