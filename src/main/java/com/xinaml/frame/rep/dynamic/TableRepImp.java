package com.xinaml.frame.rep.dynamic;

import com.xinaml.frame.base.rep.MongoRepImp;
import com.xinaml.frame.entity.dynamic.Table;
import org.springframework.stereotype.Repository;

@Repository
public class TableRepImp extends MongoRepImp<Table> implements TableRep {
}
