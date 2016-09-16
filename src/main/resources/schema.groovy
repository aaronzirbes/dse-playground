// Property keys
schema.propertyKey('name').Text().ifNotExists().create()
schema.propertyKey('age').Text().ifNotExists().create()
schema.propertyKey('reason').Text().ifNotExists().create()
schema.propertyKey('time').Timestamp().ifNotExists().create()
schema.propertyKey('place').Point().ifNotExists().create();

// Vertex labels
schema.vertexLabel('god').properties('name', 'age').ifNotExists().create()
schema.vertexLabel('demigod').properties('name', 'age').ifNotExists().create()
schema.vertexLabel('human').properties('name', 'age').ifNotExists().create()
schema.vertexLabel('monster').properties('name').ifNotExists().create()
schema.vertexLabel('location').properties('name').ifNotExists().create()
schema.vertexLabel('titan').properties('name', 'age').ifNotExists().create()

// Edge labels
schema.edgeLabel('father').connection('demigod', 'god').connection('god','titan').ifNotExists().create()
schema.edgeLabel('mother').connection('demigod', 'human').ifNotExists().create()
schema.edgeLabel('brother').connection('god', 'god').ifNotExists().create()
schema.edgeLabel('lives').properties('reason').connection('god', 'location').connection('monster', 'location').ifNotExists().create()
schema.edgeLabel('battled').properties('time', 'place').connection('demigod', 'monster').connection('god', 'god').connection('god', 'monster').ifNotExists().create()
schema.edgeLabel('pet').connection('god', 'monster').ifNotExists().create()

// Indexes
schema.vertexLabel('god').index('godByName').materialized().by('name').ifNotExists().add()
schema.vertexLabel('demigod').index('demigodByName').materialized().by('name').ifNotExists().add()
schema.vertexLabel('human').index('humanByName').materialized().by('name').ifNotExists().add()
schema.vertexLabel('monster').index('monsterByName').materialized().by('name').ifNotExists().add()
schema.vertexLabel('location').index('locationByName').materialized().by('name').ifNotExists().add()

//~ INSTANCES =================================================================

// Instances - Vertices
saturn = graph.addVertex(T.label, 'titan', 'name', 'saturn', 'age', 10000)
sky = graph.addVertex(T.label, 'location', 'name', 'sky')
sea = graph.addVertex(T.label, 'location', 'name', 'sea')
jupiter = graph.addVertex(T.label, 'god', 'name', 'jupiter', 'age', 5000)
neptune = graph.addVertex(T.label, 'god', 'name', 'neptune', 'age', 4500)
hercules = graph.addVertex(T.label, 'demigod', 'name', 'hercules', 'age', 30)
alcmene = graph.addVertex(T.label, 'human', 'name', 'alcmene', 'age', 45)
pluto = graph.addVertex(T.label, 'god', 'name', 'pluto', 'age', 4000)
nemean = graph.addVertex(T.label, 'monster', 'name', 'nemean')
hydra = graph.addVertex(T.label, 'monster', 'name', 'hydra')
cerberus = graph.addVertex(T.label, 'monster', 'name', 'cerberus')
tartarus = graph.addVertex(T.label, 'location', 'name', 'tartarus')

// Instances - Edges
jupiter.addEdge('father', saturn)
jupiter.addEdge('lives', sky, 'reason', 'loves fresh breezes')
jupiter.addEdge('brother', neptune)
jupiter.addEdge('brother', pluto)

neptune.addEdge('lives', sea, 'reason', 'loves waves')
neptune.addEdge('brother', jupiter)
neptune.addEdge('brother', pluto)
neptune.addEdge('battled', jupiter, 'time', java.time.Instant.ofEpochMilli(5))

hercules.addEdge('father', jupiter)
hercules.addEdge('mother', alcmene)
hercules.addEdge('battled', nemean, 'time', java.time.Instant.ofEpochMilli(1), 'place', 'POINT(38.1 23.7)')
hercules.addEdge('battled', hydra, 'time', java.time.Instant.ofEpochMilli(2), 'place', 'POINT(37.7 23.9)')
hercules.addEdge('battled', cerberus, 'time', java.time.Instant.ofEpochMilli(12), 'place', 'POINT(39 22)')

pluto.addEdge('brother', jupiter)
pluto.addEdge('brother', neptune)
pluto.addEdge('lives', tartarus, 'reason', 'no fear of death')
pluto.addEdge('pet', cerberus)

cerberus.addEdge('lives', tartarus)

// Set up graph config for later
schema.config().option('graph.allow_scan').set(true)

