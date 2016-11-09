// Property Keys

schema.propertyKey('system').Text().ifNotExists().create()
schema.propertyKey('username').Text().ifNotExists().create()
schema.propertyKey('created').Timestamp().properties('username', 'system').ifNotExists().create()
schema.propertyKey('updated').Timestamp().properties('username', 'system').ifNotExists().create()
schema.propertyKey('description').Text().ifNotExists().create()
schema.propertyKey('email').Text().multiple().ifNotExists().create()
schema.propertyKey('id').Uuid().ifNotExists().create()
schema.propertyKey('key').Text().ifNotExists().create()
schema.propertyKey('name').Text().ifNotExists().create()
schema.propertyKey('number').Text().ifNotExists().create()
schema.propertyKey('pfmSyncEnableDate').Timestamp().ifNotExists().create()
schema.propertyKey('serialNumber').Text().ifNotExists().create()
schema.propertyKey('status').Text().ifNotExists().create()
schema.propertyKey('type').Text().ifNotExists().create()

// Vertex Labels

// three states here deleted, disabled, active
schema.vertexLabel('state').properties('name').ifNotExists().create()

schema.vertexLabel('organization').properties('id',
                                              'name',
                                              'status',
                                              'pfmSyncEnableDate',
                                              'created',
                                              'updated').ifNotExists().create()

schema.vertexLabel('organizationalUnit')
      .properties('id', 'name', 'pfmSyncEnableDate', 'created', 'updated')
      .ifNotExists().create()

// this hold source IDs for PFM relations
schema.vertexLabel('pfmentity')
      .properties('id', 'name', 'type', 'key', 'description', 'created', 'updated')
      .ifNotExists().create()

schema.vertexLabel('device')
      .properties('id', 'name', 'serialNumber', 'created', 'updated') // Vehicle
      .ifNotExists().create()

schema.vertexLabel('display')
      .properties('id', 'name', 'created', 'updated') // Vehicle
      .ifNotExists().create()

schema.vertexLabel('driver')
      .properties('id', 'name', 'number', 'created', 'updated') // Customer, Terminal
      .ifNotExists().create()

schema.vertexLabel('terminal')
      .properties('id', 'name', 'status', 'created', 'updated') // Driver, Customer
      .ifNotExists().create()

schema.vertexLabel('user')
      .properties('id', 'name', 'email', 'created', 'updated') // Customer
      .ifNotExists().create()

schema.vertexLabel('privilege')
      .properties('id', 'name', 'created', 'updated') // User
      .ifNotExists().create()

schema.vertexLabel('tennant')
      .properties('id', 'name', 'created', 'updated')
      .ifNotExists().create()

schema.vertexLabel('vehicle')
      .properties('id', 'name', 'number', 'status', 'created', 'updated') // Customer, Device, Driver, Display
      .ifNotExists().create()

schema.vertexLabel('setting')
      .properties('id', 'name', 'created', 'updated')
      .ifNotExists().create()

// Edge Labels

schema.edgeLabel('hasUser')
      .multiple()
      .properties('created')
      .connection('organization', 'user')
      .ifNotExists().create()

schema.edgeLabel('pfmSource')
      .connection('device', 'pfmentity')
      .connection('display', 'pfmentity')
      .connection('driver', 'pfmentity')
      .connection('terminal', 'pfmentity')
      .connection('user', 'pfmentity')
      .connection('privilege', 'pfmentity')
      .connection('vehicle', 'pfmentity')
      .connection('organizationalUnit', 'pfmentity')  //rg added
      .connection('organization', 'pfmentity')  //rg added
      .ifNotExists().create()

schema.edgeLabel('state')
      .connection('organization', 'state')
      .connection('organizationalUnit', 'state')
      .connection('device', 'state')
      .connection('display', 'state')
      .connection('driver', 'state')
      .connection('terminal', 'state')
      .connection('user', 'state')
      .connection('privilege', 'state')
      .connection('vehicle', 'state')
      .ifNotExists().create()

schema.edgeLabel('memberOf')
      .connection('display', 'organization')
      .connection('driver', 'organization')
      .connection('terminal', 'organization')
      .connection('user', 'organization')
      .connection('privilege', 'organization')
      .connection('vehicle', 'organization')
      .ifNotExists().create()

schema.edgeLabel('parent')
      .single()
      .properties('created')
      .connection('organizationalUnit', 'organization')
      .connection('organizationalUnit', 'organizationalUnit')
      .ifNotExists().create()

schema.edgeLabel('child')
      .multiple()
      .properties('created')
      .connection('organization', 'organizationalUnit')
      .connection('organizationalUnit', 'organizationalUnit')
      .ifNotExists().create()

schema.edgeLabel('belongsTo')
      .single()
      .properties('created')
      .connection('user', 'organization')
      .connection('user', 'organizationalUnit')
      .ifNotExists().create()

schema.edgeLabel('installedInto')
      .single()
      .properties('created')
      .connection('device', 'vehicle')
      .connection('display', 'vehicle')
      .ifNotExists().create()

schema.edgeLabel('based')
      .single()
      .properties('created')
      .connection('driver', 'organizationalUnit')
      .connection('vehicle', 'organizationalUnit')
      .ifNotExists().create()

schema.edgeLabel('drives')
      .properties('created')
      .connection('driver', 'vehicle')
      .ifNotExists().create()

schema.edgeLabel('isUser')
      .single()
      .properties('created')
      .connection('driver', 'user')
      .ifNotExists().create()

schema.edgeLabel('ofUnit')
      .properties('created')
      .connection('terminal', 'organizationalUnit')
      .ifNotExists().create()

schema.edgeLabel('hasPrivilege')
      .properties('created')
      .connection('user', 'privilege')
      .ifNotExists().create()

schema.edgeLabel('tennantOf')
      .properties('created')
      .connection('organization', 'tennant')
      .ifNotExists().create()

schema.edgeLabel('managedBy')
      .properties('created')
      .connection('setting', 'organizationalUnit')
      .ifNotExists().create()

schema.edgeLabel('usedBy')
      .properties('created')
      .connection('setting', 'organizationalUnit')
      .ifNotExists().create()

schema.edgeLabel('availableTo')
      .properties('created')
      .connection('setting', 'organizationalUnit')
      .ifNotExists().create()

schema.edgeLabel('baseSettings')
      .properties('created')
      .connection('setting', 'organizationalUnit')
      .ifNotExists().create()

