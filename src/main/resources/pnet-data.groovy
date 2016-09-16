import java.time.Instant

import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.Edge
import org.apache.tinkerpop.gremlin.structure.VertexProperty

//~ Instances - Vertices

void addProvenance(Vertex v) {
    final String username = 'azirbes'
    final String system = 'load'

    def created = v.property('created', Instant.now())
        created.property('username', username)
        created.property('system', system)
}

// Companies
String deanId = '84f34a84-8969-4aa1-891a-fd35fbc8fa6a'
Vertex deanOrg = graph.addVertex(T.label, 'organization',
                          'name', 'Dean Foods',
                          'status', 'active',
                          'id', deanId) // not sure if this is the best way to supply this
addProvenance(deanOrg)

Vertex pfmDean = graph.addVertex(T.label, 'pfmentity',
                          'type', 'customer',
                          'key', '57',
                          'id', UUID.randomUUID().toString())

// Org Unit
String texarkanaId = '5fcd3e2c-61ef-449f-914e-a6ea099f9a77'
Vertex texarkana = graph.addVertex(T.label, 'organizationalUnit',
                            'name', 'Texarkana Foods',
                            'id', texarkanaId)
addProvenance(texarkana)

Vertex pfmTexarkana = graph.addVertex(T.label, 'pfmentity',
                          'type', 'vehicleGroup',
                          'key', '592',
                          'id', UUID.randomUUID().toString())

String coorsId = '36655140-d5d0-4e39-9cb4-b85a4a8184dc'
Vertex coors = graph.addVertex(T.label, 'terminal',
                            'name', 'Coors Distribution',
                            'id', coorsId)
addProvenance(coors)

Vertex pfmCoors = graph.addVertex(T.label, 'pfmentity',
                          'type', 'terminal',
                          'key', '80045',
                          'id', UUID.randomUUID().toString())

// Users
String zirbesId = 'fb856be0-d0c7-4371-9014-171e12ad3aa8'
Vertex zirbes = graph.addVertex(T.label, 'user',
                         'id', zirbesId,
                         'name', 'Aaron Zirbes',
                         'email', 'aaron@zirb.es')
addProvenance(zirbes)

Vertex pfmZirbes = graph.addVertex(T.label, 'pfmentity',
                            'type', 'user',
                            'key', '9511',
                            'id', UUID.randomUUID().toString())

// Devices
String onStarId = '3f1e43e1-5a16-43c0-b3d1-036847a2761a'
Vertex onStar = graph.addVertex(T.label, 'device',
                         'id', onStarId,
                         'name', 'On Star Button',
                         'serialNumber', 'ON10584034')
addProvenance(onStar)

Vertex pfmOnStar = graph.addVertex(T.label, 'pfmentity',
                            'type', 'device',
                            'key', 'ON10584034',
                            'id', UUID.randomUUID().toString())

// Vehicles
String banditId = 'aefddd6e-e8e3-42e9-97ee-9933dfc3b3b0'
Vertex bandit = graph.addVertex(T.label, 'vehicle',
                         'id', banditId,
                         'name', 'The Bandit',
                         'number', 'BANONE',
                         'status', 'active')
addProvenance(bandit)

Vertex pfmBandit = graph.addVertex(T.label, 'pfmentity',
                            'type', 'vehicle',
                            'key', 'BANONE',
                            'id', UUID.randomUUID().toString())

// Drivers
String burtId = 'ca189d27-89c5-4893-9d8c-dccabd405c07'
Vertex burt = graph.addVertex(T.label, 'driver',
                         'id', burtId,
                         'name', 'Burt Reynolds',
                         'number', 'bandit1')
addProvenance(burt)

Vertex pfmBurt = graph.addVertex(T.label, 'pfmentity',
                          'type', 'driver',
                          'key', 'bandit1',
                          'id', UUID.randomUUID().toString())

//~ Relationships - Edges

deanOrg.addEdge('hasUser', zirbes)
deanOrg.addEdge('pfmSource', pfmDean)
deanOrg.addEdge('containsUnit', texarkana)

zirbes.addEdge('belongsTo', deanOrg)
zirbes.addEdge('belongsTo', texarkana)
zirbes.addEdge('pfmSource', pfmZirbes)

bandit.addEdge('memberOf', deanOrg)
bandit.addEdge('pfmSource', pfmBandit)

texarkana.addEdge('hasVehicle', bandit)
texarkana.addEdge('pfmSource', pfmTexarkana)

coors.addEdge('ofUnit', texarkana)
coors.addEdge('pfmSource', pfmCoors)

onStar.addEdge('installedInto', bandit)
onStar.addEdge('pfmSource', pfmOnStar)

burt.addEdge('drives', bandit)
burt.addEdge('pfmSource', pfmBurt)

bandit.addEdge('hasDriver', burt)
bandit.addEdge('pfmSource', pfmBandit)

bandit.addEdge('drivesOutOf', coors)

// Queries

