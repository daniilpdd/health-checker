package hc.dao

import zio.Has

package object check {
  type CheckDao = Has[CheckDao.Service]

  object CheckDao {
    trait Service {
      //todo dao service для хранения результатов проверок
    }
  }

}
